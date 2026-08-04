import api from "./axios";

export const getInvoiceAuditHistory = (invoiceId) => {
    return api.get(`/api/audit/invoice/${invoiceId}`);
};
