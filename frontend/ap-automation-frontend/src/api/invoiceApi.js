import api from "./axios";

export const uploadInvoice = (file) => {
    const formData = new FormData();
    formData.append("file", file);

    return api.post("/api/invoices/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" }
    });
};

export const getInvoices = (status) => {
    return api.get("/api/invoices", {
        params: status ? { status } : {}
    });
};

export const getInvoiceById = (id) => {
    return api.get(`/api/invoices/${id}`);
};

// The file endpoint requires a JWT Authorization header, so it can't be
// linked to directly with a plain <a href>. Fetch it as a blob instead and
// hand the caller an object URL to open/download.
export const fetchInvoiceFileBlobUrl = async (id) => {
    const response = await api.get(`/api/invoices/${id}/file`, {
        responseType: "blob"
    });
    return URL.createObjectURL(response.data);
};
