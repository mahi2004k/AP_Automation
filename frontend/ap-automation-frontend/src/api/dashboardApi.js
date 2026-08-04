import api from "./axios";

export const getDashboardData = () => {
    return api.get("/api/dashboard");
};
