import api from "./axios";

export const runInvoiceMatch = (invoiceId) => {
    return api.post(`/api/matching/${invoiceId}`);
};
