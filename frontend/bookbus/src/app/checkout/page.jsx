"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

const CheckoutPage = () => {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  // Fetch cart from BACKEND (not localStorage)
  useEffect(() => {
    const fetchCart = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        router.push("/auth/login");
        return;
      }

      try {
        const res = await fetch("http://localhost:8080/cart", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!res.ok) throw new Error("Failed to fetch cart");
        const data = await res.json();
        setCartItems(data);
      } catch (err) {
        console.error(err);
        alert("Failed to load cart");
        router.push("/cart");
      } finally {
        setLoading(false);
      }
    };

    fetchCart();
  }, [router]);

  const handleCheckout = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      alert("Please login");
      router.push("/auth/login");
      return;
    }

    try {
      const res = await fetch("http://localhost:8080/checkout", {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) {
        const error = await res.text();
        throw new Error(error || "Checkout failed");
      }

      const bookings = await res.json();
      alert(`Success! ${bookings.length} booking(s) confirmed.`);
      router.push("/bookings");
    } catch (err) {
      console.error(err);
      alert(err.message || "Checkout failed");
    }
  };

  if (loading) return <p className="text-center mt-10">Loading...</p>;
  if (cartItems.length === 0)
    return <p className="text-center mt-10 text-red-600">Cart is empty</p>;

  const grandTotal = cartItems.reduce(
    (sum, item) => sum + item.seats * item.route.pricePerSeat,
    0
  );

  return (
    <div className="container mx-auto p-4 max-w-5xl">
      <h1 className="text-3xl font-bold mb-6">Checkout</h1>

      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
        {cartItems.map((item) => (
          <Card key={item.id} className="shadow-lg">
            <CardHeader>
              <CardTitle>{item.route.origin} → {item.route.destination}</CardTitle>
            </CardHeader>
            <CardContent>
              <p><strong>Operator:</strong> {item.route.busOperator}</p>
              <p><strong>Seats:</strong> {item.seats}</p>
              <p><strong>Price/seat:</strong> ₹{item.route.pricePerSeat}</p>
              <p className="font-bold">
                Total: ₹{item.seats * item.route.pricePerSeat}
              </p>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="border-t pt-6">
        <div className="flex justify-between text-xl font-bold mb-6">
          <span>Grand Total:</span>
          <span>₹{grandTotal}</span>
        </div>

        <Button
          className="w-full bg-green-600 hover:bg-green-700"
          onClick={handleCheckout}
        >
          Confirm & Pay
        </Button>
      </div>
    </div>
  );
};

export default CheckoutPage;