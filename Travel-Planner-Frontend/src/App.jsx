import { Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import Register from "./pages/Register";
import OAuthSuccess from "./pages/OAuthSuccess";
import Dashboard from "./pages/Dashboard";
import FlightSearch from "./pages/FlightSearch";


import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <Routes>
      {/* ================= Public Routes ================= */}
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/oauth-success" element={<OAuthSuccess />} />

      {/* ================= Protected Routes ================= */}
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        }
      />

      {/* Flights */}
      <Route
        path="/flights"
        element={
          <ProtectedRoute>
            <FlightSearch />
          </ProtectedRoute>
        }
      />

    </Routes>
  );
}

export default App;
