import api from "./axios";

export const getPendingApprovals = () => {
    return api.get("/api/approvals/pending");
};

export const approveInvoice = (invoiceId, remarks) => {
    return api.post(`/api/approvals/${invoiceId}/approve`, { remarks });
};

export const rejectInvoice = (invoiceId, remarks) => {
    return api.post(`/api/approvals/${invoiceId}/reject`, { remarks });
};
