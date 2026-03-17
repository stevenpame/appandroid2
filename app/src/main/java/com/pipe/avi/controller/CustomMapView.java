package com.pipe.avi.controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.pipe.avi.R;

import java.util.ArrayList;
import java.util.List;

public class CustomMapView extends SubsamplingScaleImageView {

    private final List<Marcador> marcadores = new ArrayList<>();
    private final PointF vPoint = new PointF();
    private final Paint textPaint = new Paint();
    private final Paint bubblePaint = new Paint();
    private final Paint btnPaint = new Paint();
    private Drawable pinDrawable;
    
    private int selectedMarkerId = -1; // Controla qué globo mostrar
    private final GestureDetector gestureDetector;
    private OnARClickListener arClickListener;

    public interface OnARClickListener {
        void onARClick(Marcador marcador);
    }

    public static class Marcador {
        int id;
        PointF punto;
        String nombre;
        String descripcion;

        Marcador(int id, PointF punto, String nombre, String descripcion) {
            this.id = id;
            this.punto = punto;
            this.nombre = nombre;
            this.descripcion = descripcion;
        }
    }

    public CustomMapView(Context context) {
        this(context, null);
    }

    public CustomMapView(Context context, AttributeSet attr) {
        super(context, attr);
        
        pinDrawable = ContextCompat.getDrawable(context, R.drawable.ic_location);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(32);
        textPaint.setFakeBoldText(true);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        bubblePaint.setColor(Color.parseColor("#121212"));
        bubblePaint.setStyle(Paint.Style.FILL);
        bubblePaint.setAntiAlias(true);

        btnPaint.setColor(Color.parseColor("#FFD600")); // Amarillo para el botón AR
        btnPaint.setStyle(Paint.Style.FILL);
        btnPaint.setAntiAlias(true);

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isReady()) {
                    float clickX = e.getX();
                    float clickY = e.getY();

                    // 1. Si hay un globo abierto, verificar si se tocó el botón AR
                    if (selectedMarkerId != -1) {
                        for (Marcador m : marcadores) {
                            if (m.id == selectedMarkerId) {
                                sourceToViewCoord(m.punto, vPoint);
                                RectF btnRect = getARButtonRect(vPoint.x, vPoint.y - 70, m.nombre);
                                if (btnRect.contains(clickX, clickY)) {
                                    if (arClickListener != null) arClickListener.onARClick(m);
                                    return true;
                                }
                            }
                        }
                    }

                    // 2. Verificar si se tocó un pin para mostrar su información
                    boolean hit = false;
                    for (Marcador m : marcadores) {
                        sourceToViewCoord(m.punto, vPoint);
                        RectF touchArea = new RectF(vPoint.x - 50, vPoint.y - 100, vPoint.x + 50, vPoint.y + 20);
                        if (touchArea.contains(clickX, clickY)) {
                            selectedMarkerId = m.id;
                            hit = true;
                            break;
                        }
                    }
                    
                    if (!hit) selectedMarkerId = -1;
                    invalidate(); // Redibujar para mostrar/ocultar globo
                    return hit;
                }
                return false;
            }
        });
    }

    public void setOnARClickListener(OnARClickListener listener) {
        this.arClickListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    public void addMarcadorPorPorcentaje(int id, float leftPct, float topPct, String nombre, String descripcion) {
        if (isReady()) {
            float x = (leftPct / 100f) * getSWidth();
            float y = (topPct / 100f) * getSHeight();
            this.marcadores.add(new Marcador(id, new PointF(x, y), nombre, descripcion));
            invalidate();
        }
    }

    public void clearMarcadores() {
        marcadores.clear();
        selectedMarkerId = -1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady() || pinDrawable == null) return;

        for (Marcador m : marcadores) {
            sourceToViewCoord(m.punto, vPoint);
            
            int pinSize = 70;
            pinDrawable.setBounds((int)vPoint.x - pinSize/2, (int)vPoint.y - pinSize, (int)vPoint.x + pinSize/2, (int)vPoint.y);
            pinDrawable.draw(canvas);

            // Solo mostrar el globo si el marcador está seleccionado
            if (m.id == selectedMarkerId) {
                drawInfoBubbleWithButton(canvas, m.nombre, vPoint.x, vPoint.y - pinSize);
            }
        }
    }

    private RectF getARButtonRect(float x, float y, String title) {
        float triangleH = 15;
        // Botón más grande (180x70)
        float btnW = 180;
        float btnH = 70;
        float bubbleBottomY = y - triangleH - 10;
        return new RectF(x - btnW / 2, bubbleBottomY - btnH - 10, x + btnW / 2, bubbleBottomY - 10);
    }

    private void drawInfoBubbleWithButton(Canvas canvas, String title, float x, float y) {
        float padding = 25;
        float triangleH = 15;
        float btnSpace = 90; // Más espacio para el botón grande
        
        Rect textBounds = new Rect();
        textPaint.getTextBounds(title, 0, title.length(), textBounds);
        
        float bw = Math.max(textBounds.width() + padding * 2, 200); 
        float bh = textBounds.height() + padding + btnSpace;
        
        RectF bubbleRect = new RectF(x - bw/2, y - bh - triangleH, x + bw/2, y - triangleH);
        canvas.drawRoundRect(bubbleRect, 20, 20, bubblePaint);
        
        // Triángulo
        Path path = new Path();
        path.moveTo(x - 15, y - triangleH);
        path.lineTo(x + 15, y - triangleH);
        path.lineTo(x, y);
        path.close();
        canvas.drawPath(path, bubblePaint);
        
        // Texto
        float titleY = bubbleRect.top + padding + textBounds.height();
        canvas.drawText(title, x, titleY, textPaint);

        // Botón AR más grande
        RectF btnRect = getARButtonRect(x, y, title);
        canvas.drawRoundRect(btnRect, 15, 15, btnPaint);
        
        Paint btnTextPaint = new Paint(textPaint);
        btnTextPaint.setColor(Color.BLACK);
        btnTextPaint.setTextSize(28);
        canvas.drawText("VER AR", x, btnRect.centerY() + 10, btnTextPaint);
    }
}