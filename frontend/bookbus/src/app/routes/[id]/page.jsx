"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

const RouteDetailsPage = () => {
  const { id } = useParams();
  const [route, setRoute] = useState(null);
  const [seats, setSeats] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const router = useRouter();

  useEffect(() => {
    const fetchRoute = async () => {
      setLoading(true);
      setError("");

      if (!id || isNaN(id)) {
        setError("Invalid route ID");
        setLoading(false);
        return;
      }

      const token = localStorage.getItem("token");
      const url = `http://localhost:8080/routes/${id}`;

      console.log("Fetching route:", url);
      console.log("JWT Token:", token ? token.substring(0, 20) + "..." : "NONE");

      try {
        const res = await fetch(url, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            ...(token && { Authorization: `Bearer ${token}` }),
          },
          credentials: "include",
        });

        // DEBUG: Log everything
        console.log("Response Status:", res.status);
        console.log("Response OK:", res.ok);
        console.log("Response Headers:", Object.fromEntries(res.headers.entries()));

        // Try to read body even on error
        let bodyText = "";
        try {
          bodyText = await res.text();
          console.log("Raw Response Body:", bodyText);
        } catch (e) {
          console.log("Could not read response body");
        }

        if (!res.ok) {
          let errorMsg = `HTTP ${res.status}`;

          // Try to parse JSON error
          try {
            const errorJson = JSON.parse(bodyText);
            errorMsg = errorJson.message || errorJson.error || errorMsg;
          } catch {
            // If not JSON, use text
            if (bodyText.includes("login")) errorMsg = "Please log in";
            if (bodyText.includes("not found")) errorMsg = "Route not found";
          }

          console.error("API Error:", errorMsg);
          throw new Error(errorMsg);
        }

        // Success: parse JSON
        const data = JSON.parse(bodyText);
        console.log("Parsed Route Data:", data);
        setRoute(data);
      } catch (err) {
        console.error("Fetch failed:", err);
        setError(err.message || "Network error");
      } finally {
        setLoading(false);
      }
    };

    fetchRoute();
  }, [id]);

  // === ADD TO CART (SERVER-SIDE) ===
  const handleAddToCart = async () => {
    const requestedSeats = parseInt(seats);
    if (requestedSeats > route.seatsAvailable) {
      alert("Not enough seats");
      return;
    }

    try {
      const token = localStorage.getItem("token");
      const res = await fetch(`http://localhost:8080/cart/${route.id}?seats=${requestedSeats}`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (!res.ok) throw new Error("Failed to add to cart");
      alert("Added to cart!");
      router.push("/cart");
    } catch (err) {
      alert("Login required");
      router.push("/auth/login");
    }
  };

  // === UI ===
  if (loading) return <p className="text-center mt-10">Loading...</p>;
  if (error) {
    return (
      <div className="text-center mt-10 p-6 bg-red-50 rounded-lg">
        <p className="text-red-600 font-bold text-lg">{error}</p>
        <details className="mt-4 text-left text-sm bg-white p-4 rounded">
          <summary className="cursor-pointer font-semibold">Debug Info (click)</summary>
          <pre className="mt-2 text-xs overflow-auto">
            {`ID: ${id}\nURL: http://localhost:8080/routes/${id}\nToken: ${localStorage.getItem("token") ? "Present" : "Missing"}`}
          </pre>
        </details>
        <Button onClick={() => router.push("/routes")} className="mt-4">
          Back to Routes
        </Button>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-4">
      <Button className="mb-4" onClick={() => router.back()}>Back</Button>

      <Card className="shadow-lg">
        <CardHeader>
          <CardTitle>{route.busOperator} ({route.busType})</CardTitle>
        </CardHeader>
        <CardContent>
          <p><strong>From:</strong> {route.origin}</p>
          <p><strong>To:</strong> {route.destination}</p>
          <p><strong>Dep:</strong> {route.departureTime}</p>
          <p><strong>Arr:</strong> {route.arrivalTime}</p>
          <p><strong>Seats Left:</strong> {route.seatsAvailable}</p>
          <p><strong>Price:</strong> ₹{route.pricePerSeat}</p>
          <p><strong>Amenities:</strong> {route.amenities || "None"}</p>

          <div className="mt-6 flex items-center gap-4">
            <input
              type="number"
              min="1"
              max={route.seatsAvailable}
              value={seats}
              onChange={(e) => setSeats(Math.max(1, Math.min(parseInt(e.target.value) || 1, route.seatsAvailable)))}
              className="w-20 p-2 border rounded"
            />
            <Button onClick={handleAddToCart} className="bg-green-600">
              Add to Cart ({seats})
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default RouteDetailsPage;