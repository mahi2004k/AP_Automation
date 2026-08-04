import api from "./axios";

export const getPurchaseOrders = () => {
    return api.get("/api/purchase-orders");
};

export const getPurchaseOrderById = (id) => {
    return api.get(`/api/purchase-orders/${id}`);
};

export const createPurchaseOrder = (data) => {
    return api.post("/api/purchase-orders", data);
};

export const updatePurchaseOrder = (id, data) => {
    return api.put(`/api/purchase-orders/${id}`, data);
};

export const deletePurchaseOrder = (id) => {
    return api.delete(`/api/purchase-orders/${id}`);
};