import { NavLink } from "react-router-dom";

function Sidebar() {

    return (

        <div
            className="bg-dark text-white p-3"
            style={{
                width: "250px",
                height: "100vh",
                position: "fixed",
                top: "56px",
                left: 0
            }}
        >

            <h5 className="mb-4">

                Menu

            </h5>

            <NavLink
                to="/dashboard"
                className="d-block text-white text-decoration-none mb-3"
            >
                Dashboard
            </NavLink>

            <NavLink
                to="/purchase-orders"
                className="d-block text-white text-decoration-none mb-3"
            >
                Purchase Orders
            </NavLink>

            <NavLink
                to="/receiving-reports"
                className="d-block text-white text-decoration-none mb-3"
            >
                Receiving Reports
            </NavLink>

            <NavLink
                to="/invoices"
                className="d-block text-white text-decoration-none mb-3"
            >
                Invoices
            </NavLink>

            <NavLink
                to="/matching"
                className="d-block text-white text-decoration-none mb-3"
            >
                Matching
            </NavLink>

            <NavLink
                to="/approvals"
                className="d-block text-white text-decoration-none mb-3"
            >
                Approvals
            </NavLink>

            <NavLink
                to="/payments"
                className="d-block text-white text-decoration-none mb-3"
            >
                Payments
            </NavLink>

            <NavLink
                to="/audit"
                className="d-block text-white text-decoration-none"
            >
                Audit Logs
            </NavLink>

        </div>

    );

}

export default Sidebar;