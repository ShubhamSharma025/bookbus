// app/bookings/page.jsx
"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { getUserBookings } from "@/services/bookingService";

export default function BookingsPage() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const router = useRouter();

  useEffect(() => {
    const fetchBookings = async () => {
      try {
        const data = await getUserBookings();
        setBookings(data);
      } catch (err) {
        if (err.response?.status === 401) {
          router.push("/auth/login");
        } else {
          setError("Failed to load bookings.");
        }
      } finally {
        setLoading(false);
      }
    };
    fetchBookings();
  }, [router]);

  if (loading) return <p className="text-center mt-10">Loading...</p>;
  if (error) return <p className="text-center mt-10 text-red-600">{error}</p>;
  if (bookings.length === 0)
    return <p className="text-center mt-10 text-gray-600">No bookings yet.</p>;

  return (
    <div className="container mx-auto p-6 max-w-6xl">
      <h1 className="text-4xl font-bold mb-8 text-center text-blue-800">
        My Bookings
      </h1>

      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {bookings.map((booking) => (
          <Card
            key={booking.id}
            className="shadow-lg hover:shadow-xl transition-shadow border"
          >
            <CardHeader className="bg-blue-50">
              <CardTitle className="text-lg">Booking #{booking.id}</CardTitle>
            </CardHeader>
            <CardContent className="pt-4 space-y-1">
              <p><strong>Route:</strong> {booking.routeId}</p>
              <p><strong>Seats:</strong> {booking.seats}</p>
              <p><strong>Total:</strong> ₹{booking.totalAmount}</p>
              <p><strong>Date:</strong> {new Date(booking.bookingDate).toLocaleDateString()}</p>
              <p className={`font-bold ${booking.status === 'CONFIRMED' ? 'text-green-600' : 'text-red-600'}`}>
                {booking.status}
              </p>

              <Button
                className="mt-3 w-full bg-blue-600 hover:bg-blue-700"
                onClick={() => router.push(`/bookings/${booking.id}`)}
              >
                View Details
              </Button>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}