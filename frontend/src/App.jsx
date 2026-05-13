import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import AuthPage from "./components/AuthPage";
import AdminDashboard from "./components/AdminDashboard";
import DoctorDashboard from "./components/DoctorDashboard";
import NurseDashboard from "./components/NurseDashboard";
import ReceptionistDashboard from "./components/ReceptionistDashboard";
import BillingDashboard from "./components/BillingDashboard";
import ComplianceDashboard from "./components/ComplianceDashboard";

/**
 * ✅ Protected Route Component
 * - Checks token existence
 * - Checks role match (case-insensitive)
 */
function ProtectedRoute({ children, role }) {
  const token = localStorage.getItem("accessToken");
  const userRole = localStorage.getItem("role"); // stored as lowercase

  // 🔒 Not authenticated
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  // 🔒 Wrong role
  if (role && userRole?.toLowerCase() !== role.toLowerCase()) {
    return <Navigate to="/login" replace />;
  }

  // ✅ Authorized
  return children;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* ✅ Default */}
        <Route path="/" element={<Navigate to="/login" replace />} />

        {/* ✅ Auth */}
        <Route path="/login" element={<AuthPage />} />

        {/* ✅ Admin Dashboard */}
        <Route
          path="/dashboard/admin"
          element={
            <ProtectedRoute role="admin">
              <AdminDashboard />
            </ProtectedRoute>
          }
        />

        {/* ✅ Doctor Dashboard */}
        <Route
          path="/dashboard/doctor"
          element={
            <ProtectedRoute role="doctor">
              <DoctorDashboard />
            </ProtectedRoute>
          }
        />

        {/* ✅ Nurse Dashboard */}
        <Route
          path="/dashboard/nurse"
          element={
            <ProtectedRoute role="nurse">
              <NurseDashboard />
            </ProtectedRoute>
          }
        />

        {/* ✅ Receptionist Dashboard */}
        <Route
          path="/dashboard/reception"
          element={
            <ProtectedRoute role="reception">
              <ReceptionistDashboard />
            </ProtectedRoute>
          }
        />

        {/* ✅ Billing Dashboard */}
        <Route
          path="/dashboard/billing"
          element={
            <ProtectedRoute role="billing">
              <BillingDashboard />
            </ProtectedRoute>
          }
        />

        {/* ✅ Compliance Dashboard */}
        <Route
          path="/dashboard/compliance"
          element={
            <ProtectedRoute role="compliance_officer">
              <ComplianceDashboard />
            </ProtectedRoute>
          }
        />

        {/* ✅ Fallback */}
        <Route path="*" element={<Navigate to="/login" replace />} />

      </Routes>
    </BrowserRouter>
  );
}