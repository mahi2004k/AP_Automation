import api from "./axios";

export const getReceivingReports = () => {
    return api.get("/api/receiving-reports");
};

export const getReceivingReportById = (id) => {
    return api.get(`/api/receiving-reports/${id}`);
};

export const createReceivingReport = (data) => {
    return api.post("/api/receiving-reports", data);
};

export const updateReceivingReport = (id, data) => {
    return api.put(`/api/receiving-reports/${id}`, data);
};

export const deleteReceivingReport = (id) => {
    return api.delete(`/api/receiving-reports/${id}`);
};
