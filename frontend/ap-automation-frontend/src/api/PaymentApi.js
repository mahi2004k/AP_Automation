import api from "./axios";

export const makePayment = (invoiceId, data) => {
    return api.post(`/api/payments/${invoiceId}`, data);
};

export const getPaymentById = (paymentId) => {
    return api.get(`/api/payments/${paymentId}`);
};

export const getPaymentByInvoice = (invoiceId) => {
    return api.get(`/api/payments/invoice/${invoiceId}`);
};

export const getPayments = () => {
    return api.get("/api/payments");
};
