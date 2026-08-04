import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import DashboardLayout from "../../layouts/DashboardLayout";
import { uploadInvoice } from "../../api/invoiceApi";

function UploadInvoice() {

    const navigate = useNavigate();

    const [file, setFile] = useState(null);
    const [uploading, setUploading] = useState(false);

    const handleSubmit = async (e) => {

        e.preventDefault();

        if (!file) {
            toast.error("Please choose a PDF invoice to upload.");
            return;
        }

        if (file.type !== "application/pdf") {
            toast.error("Only PDF invoices are supported.");
            return;
        }

        setUploading(true);

        try {

            const response = await uploadInvoice(file);

            toast.success(response.data?.message || "Invoice uploaded and processed successfully");

            navigate(`/invoices/${response.data.id}`);

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                error.response?.data ||
                "Failed to upload invoice"
            );

        } finally {

            setUploading(false);

        }

    };

    return (
        <DashboardLayout>

            <h2 className="mb-4">Upload Invoice</h2>

            <div className="card" style={{ maxWidth: 560 }}>

                <div className="card-body">

                    <p className="text-muted">
                        Upload a text-based PDF invoice. It will be scanned automatically
                        to extract the vendor, invoice number, PO number, line items and
                        totals, then matched against any purchase order on file.
                    </p>

                    <form onSubmit={handleSubmit}>

                        <div className="mb-3">
                            <label className="form-label">Invoice PDF</label>
                            <input
                                type="file"
                                accept="application/pdf"
                                className="form-control"
                                onChange={(e) => setFile(e.target.files?.[0] || null)}
                            />
                        </div>

                        <button type="submit" className="btn btn-primary" disabled={uploading}>
                            {uploading ? "Processing..." : "Upload & Extract"}
                        </button>

                    </form>

                </div>

            </div>

        </DashboardLayout>
    );
}

export default UploadInvoice;
