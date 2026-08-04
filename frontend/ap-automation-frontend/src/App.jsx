import { BrowserRouter, Routes, Route } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import Dashboard from "./pages/dashboard/Dashboard";
import PrivateRoute from "./components/PrivateRoute";

import PurchaseOrderList from "./pages/purchaseOrders/PurchaseOrderList";
import AddPurchaseOrder from "./pages/purchaseOrders/AddPurchaseOrder";
import EditPurchaseOrder from "./pages/purchaseOrders/EditPurchaseOrder";

import InvoiceList from "./pages/invoices/InvoiceList";
import UploadInvoice from "./pages/invoices/UploadInvoice";
import InvoiceDetail from "./pages/invoices/InvoiceDetail";

import ReceivingReportList from "./pages/receivingReports/ReceivingReportList";
import AddReceivingReport from "./pages/receivingReports/AddReceivingReport";

import MatchingList from "./pages/matching/MatchingList";
import ApprovalList from "./pages/approvals/ApprovalList";
import PaymentList from "./pages/payments/PaymentList";
import AuditLogList from "./pages/audit/AuditLogList";

function App() {

    return (

        <BrowserRouter>

            <ToastContainer position="top-right" />

            <Routes>

                <Route path="/" element={<Login />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                <Route
                    path="/dashboard"
                    element={
                        <PrivateRoute>
                            <Dashboard />
                        </PrivateRoute>
                    }
                />

                {/* Purchase Orders */}
                <Route
                    path="/purchase-orders"
                    element={
                        <PrivateRoute>
                            <PurchaseOrderList />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/purchase-orders/add"
                    element={
                        <PrivateRoute>
                            <AddPurchaseOrder />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/purchase-orders/edit/:id"
                    element={
                        <PrivateRoute>
                            <EditPurchaseOrder />
                        </PrivateRoute>
                    }
                />

                {/* Invoices */}
                <Route
                    path="/invoices"
                    element={
                        <PrivateRoute>
                            <InvoiceList />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/invoices/upload"
                    element={
                        <PrivateRoute>
                            <UploadInvoice />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/invoices/:id"
                    element={
                        <PrivateRoute>
                            <InvoiceDetail />
                        </PrivateRoute>
                    }
                />

                {/* Receiving Reports */}
                <Route
                    path="/receiving-reports"
                    element={
                        <PrivateRoute>
                            <ReceivingReportList />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/receiving-reports/add"
                    element={
                        <PrivateRoute>
                            <AddReceivingReport />
                        </PrivateRoute>
                    }
                />

                {/* Matching */}
                <Route
                    path="/matching"
                    element={
                        <PrivateRoute>
                            <MatchingList />
                        </PrivateRoute>
                    }
                />

                {/* Approvals */}
                <Route
                    path="/approvals"
                    element={
                        <PrivateRoute>
                            <ApprovalList />
                        </PrivateRoute>
                    }
                />

                {/* Payments */}
                <Route
                    path="/payments"
                    element={
                        <PrivateRoute>
                            <PaymentList />
                        </PrivateRoute>
                    }
                />

                {/* Audit */}
                <Route
                    path="/audit"
                    element={
                        <PrivateRoute>
                            <AuditLogList />
                        </PrivateRoute>
                    }
                />

            </Routes>

        </BrowserRouter>

    );

}

export default App;
