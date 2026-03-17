package com.pipe.avi.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class ResultResponse implements Serializable {

    @SerializedName("reporte")
    private Reporte reporte;

    @SerializedName("resultadoIA")
    private ResultadoIA resultadoIA;

    public Reporte getReporte() {
        return reporte;
    }

    public ResultadoIA getResultadoIA() {
        return resultadoIA;
    }

    // ---------------------------
    // REPORTE
    // ---------------------------
    public static class Reporte implements Serializable {
        @SerializedName("idREPORTE")
        private int idREPORTE;

        @SerializedName("puntajeR")
        private int puntajeR;

        @SerializedName("puntajeI")
        private int puntajeI;

        @SerializedName("puntajeA")
        private int puntajeA;

        @SerializedName("puntajeS")
        private int puntajeS;

        @SerializedName("puntajeE")
        private int puntajeE;

        @SerializedName("puntajeC")
        private int puntajeC;

        public int getIdREPORTE() { return idREPORTE; }
        public int getPuntajeR() { return puntajeR; }
        public int getPuntajeI() { return puntajeI; }
        public int getPuntajeA() { return puntajeA; }
        public int getPuntajeS() { return puntajeS; }
        public int getPuntajeE() { return puntajeE; }
        public int getPuntajeC() { return puntajeC; }
    }

    // ---------------------------
    // RESULTADO IA
    // ---------------------------
    public static class ResultadoIA implements Serializable {

        @SerializedName("recommendations")
        private List<Recommendation> recommendations;

        public List<Recommendation> getRecommendations() {
            return recommendations;
        }
    }

    // ---------------------------
    // RECOMENDACIONES
    // ---------------------------
    public static class Recommendation implements Serializable {

        @SerializedName("idRECOMENDACION")
        private int idRECOMENDACION; // 🔑 ID real del backend

        @SerializedName("name")
        private String name;

        @SerializedName("reason")
        private String reason;

        @SerializedName("ranking")
        private Integer ranking; // puede ser null

        @SerializedName("programaId")
        private int programaId;

        @SerializedName("reporteId")
        private int reporteId;

        @SerializedName("AR")
        private String AR; // opcional

        // Getters y setters
        public int getIdRECOMENDACION() { return idRECOMENDACION; }
        public void setIdRECOMENDACION(int idRECOMENDACION) { this.idRECOMENDACION = idRECOMENDACION; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }

        public Integer getRanking() { return ranking; }
        public void setRanking(Integer ranking) { this.ranking = ranking; }

        public int getProgramaId() { return programaId; }
        public void setProgramaId(int programaId) { this.programaId = programaId; }

        public int getReporteId() { return reporteId; }
        public void setReporteId(int reporteId) { this.reporteId = reporteId; }

        public String getAR() { return AR; }
        public void setAR(String AR) { this.AR = AR; }
    }
}