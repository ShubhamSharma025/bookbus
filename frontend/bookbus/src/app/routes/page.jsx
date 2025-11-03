"use client";

import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { getAllRoutes, searchRoutes } from "@/services/routeService"; // Import API

const RoutesPage = () => {
  const [routes, setRoutes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const router = useRouter();
  const searchParams = useSearchParams();
  const from = searchParams.get("from");
  const to = searchParams.get("to");

  useEffect(() => {
    const fetchRoutes = async () => {
      setLoading(true);
      setError("");

      try {
        let data;

        if (from && to) {
          // Use today's date if not provided
          const today = new Date().toISOString().split("T")[0];
          const response = await searchRoutes(from, to, today);
          data = response.data;
        } else {
          const response = await getAllRoutes();
          data = response.data;
        }

        setRoutes(data);
      } catch (err) {
        console.error("Failed to fetch routes:", err);
        setError(
          err.response?.data?.message || "Failed to load routes. Please log in."
        );
        setRoutes([]);
      } finally {
        setLoading(false);
      }
    };

    fetchRoutes();
  }, [from, to]);

  // Loading
  if (loading) {
    return <p className="text-center mt-10">Loading bus routes...</p>;
  }

  // Error (e.g., not logged in)
  if (error) {
    return (
      <div className="text-center mt-10">
        <p className="text-red-600 font-semibold">{error}</p>
        <Button
          onClick={() => router.push("/auth/login")}
          className="mt-4"
        >
          Login to Continue
        </Button>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">
        {from && to ? `${from} to ${to}` : "All Bus Routes"}
      </h1>

      {routes.length === 0 ? (
        <p className="text-center text-red-600 font-semibold mt-10">
          No routes available for the selected cities.
        </p>
      ) : (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {routes.map((route) => (
            <Card key={route.id} className="shadow-lg hover:shadow-xl transition-shadow">
              <CardHeader>
                <CardTitle>{route.busOperator} ({route.busType})</CardTitle>
              </CardHeader>
              <CardContent>
                <p><strong>From:</strong> {route.origin}</p>
                <p><strong>To:</strong> {route.destination}</p>
                <p><strong>Departure:</strong> {route.departureTime}</p>
                <p><strong>Arrival:</strong> {route.arrivalTime}</p>
                <p><strong>Price:</strong> ₹{route.pricePerSeat}</p>
                <Button
                  className="mt-4 w-full"
                  onClick={() => router.push(`/routes/${route.id}`)}
                >
                  View Details
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};

export default RoutesPage;