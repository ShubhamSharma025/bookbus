// app/bookings/[id]/page.jsx
"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { getBookingById, cancelBooking } from "@/services/bookingService";

const BookingDetailsPage = () => {
  const { id } = useParams();
  const [booking, setBooking] = useState(null);
  const [loading, setLoading] = useState(true);
  const [cancelling, setCancelling] = useState(false);
  const router = useRouter();

  useEffect(() => {
    const fetchBooking = async () => {
      try {
        const data = await getBookingById(id);
        setBooking(data);
      } catch (err) {
        console.error(err);
        router.push("/bookings");
      } finally {
        setLoading(false);
      }
    };
    if (id) fetchBooking();
  }, [id, router]);

  const handleCancel = async () => {
    if (!confirm("Are you sure you want to cancel this booking?")) return;

    setCancelling(true);
    try {
      await cancelBooking(id);
      alert("Booking cancelled successfully.");
      router.push("/bookings");
    } catch (err) {
      alert("Failed to cancel booking.");
    } finally {
      setCancelling(false);
    }
  };

  if (loading) return <p className="text-center mt-10">Loading...</p>;
  if (!booking) return <p className="text-center mt-10 text-red-600">Booking not found.</p>;

  return (
    <div className="container mx-auto p-6 max-w-2xl">
      <Button variant="outline" className="mb-6" onClick={() => router.back()}>
        Back
      </Button>

      <Card className="shadow-xl">
        <CardHeader className="bg-blue-50">
          <CardTitle className="text-2xl">Booking #{booking.id}</CardTitle>
        </CardHeader>
        <CardContent className="pt-6 space-y-4">
          <p><strong>Route ID:</strong> {booking.routeId}</p>
          <p><strong>Seats:</strong> {booking.seats}</p>
          <p><strong>Total Amount:</strong> ₹{booking.totalAmount}</p>
          <p><strong>Booking Date:</strong> {new Date(booking.bookingDate).toLocaleString()}</p>
          <p className={`text-lg font-bold ${booking.status === 'CONFIRMED' ? 'text-green-600' : 'text-red-600'}`}>
            Status: {booking.status}
          </p>

          {booking.status === "CONFIRMED" && (
            <Button
              variant="destructive"
              className="w-full mt-6"
              onClick={handleCancel}
              disabled={cancelling}
            >
              {cancelling ? "Cancelling..." : "Cancel Booking"}
            </Button>
          )}

          {booking.status === "CANCELLED" && (
            <p className="text-center text-red-600 font-semibold mt-4">
              This booking has been cancelled.
            </p>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default BookingDetailsPage;