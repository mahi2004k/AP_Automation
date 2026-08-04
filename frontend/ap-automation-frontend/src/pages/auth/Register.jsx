import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { registerUser } from "../../api/authApi";
import { toast } from "react-toastify";
import { useAuth } from "../../context/AuthContext";


function Register() {

    const navigate = useNavigate();

    const { isAuthenticated } = useAuth();

    const [form, setForm] = useState({
        fullName: "",
        email: "",
        password: "",
        role: "ACCOUNTANT"
    });

    useEffect(() => {

        if (isAuthenticated) {
            navigate("/dashboard", { replace: true });
        }

    }, [isAuthenticated, navigate]);

    const handleChange = (e) => {

        setForm({
            ...form,
            [e.target.name]: e.target.value
        });

    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            await registerUser(form);

            toast.success("Registration Successful");

            navigate("/login", { replace: true });

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                error.response?.data ||
                "Registration Failed"
            );

        }

    };

    return (

        <div className="auth-container">

            <div className="card auth-card p-4">

                <div className="text-center mb-4">

                    <div className="logo">
                        AP Automation
                    </div>

                    <small>
                        Create your account
                    </small>

                </div>

                <form onSubmit={handleSubmit}>

                    <div className="mb-3">

                        <label>Full Name</label>

                        <input
                            type="text"
                            name="fullName"
                            className="form-control"
                            value={form.fullName}
                            onChange={handleChange}
                            required
                        />

                    </div>

                    <div className="mb-3">

                        <label>Email</label>

                        <input
                            type="email"
                            name="email"
                            className="form-control"
                            value={form.email}
                            onChange={handleChange}
                            autoComplete="email"
                            required
                        />

                    </div>

                    <div className="mb-3">

                        <label>Password</label>

                        <input
                            type="password"
                            name="password"
                            className="form-control"
                            value={form.password}
                            onChange={handleChange}
                            autoComplete="new-password"
                            required
                        />

                    </div>

                    <div className="mb-3">

                        <label>Role</label>

                        <select
                            name="role"
                            className="form-select"
                            value={form.role}
                            onChange={handleChange}
                            required
                        >
                            <option value="ACCOUNTANT">Accountant</option>
                            <option value="MANAGER">Manager</option>
                            <option value="ADMIN">Admin</option>
                        </select>

                    </div>

                    <button className="btn btn-primary w-100">
                        Register
                    </button>

                </form>

                <div className="text-center mt-3">

                    Already have an account?

                    <Link to="/login">
                        Login
                    </Link>

                </div>

            </div>

        </div>

    );

}

export default Register;
