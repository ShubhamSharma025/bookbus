"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Trash2, ShoppingCart } from "lucide-react";
import {
  getCart,
  removeFromCart,
  clearCart,
} from "@/services/routeService";

const CartPage = () => {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const router = useRouter();

  // Fetch cart on mount
  useEffect(() => {
    const fetchCart = async () => {
      setLoading(true);
      setError("");
      try {
        const res = await getCart();
        setCartItems(res.data);
      } catch (err) {
        console.error(err);
        if (err.response?.status === 401) {
          setError("Please log in to view your cart.");
          setTimeout(() => router.push("/auth/login"), 2000);
        } else {
          setError("Failed to load cart.");
        }
      } finally {
        setLoading(false);
      }
    };
    fetchCart();
  }, [router]);

  const handleRemove = async (id) => {
    try {
      await removeFromCart(id);
      setCartItems((prev) => prev.filter((item) => item.id !== id));
    } catch (err) {
      alert("Failed to remove item");
    }
  };

  const handleClearCart = async () => {
    if (!confirm("Clear entire cart?")) return;
    try {
      await clearCart();
      setCartItems([]);
    } catch (err) {
      alert("Failed to clear cart");
    }
  };

  const totalPrice = cartItems.reduce(
    (sum, item) => sum + item.route.pricePerSeat * item.seats,
    0
  );

  if (loading) return <p className="text-center mt-10">Loading cart...</p>;
  if (error) {
    return (
      <div className="text-center mt-10">
        <p className="text-red-600 font-semibold">{error}</p>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-4 max-w-4xl">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold flex items-center gap-2">
          <ShoppingCart className="w-8 h-8" />
          Your Cart
        </h1>
        {cartItems.length > 0 && (
          <Button
            variant="destructive"
            size="sm"
            onClick={handleClearCart}
          >
            Clear Cart
          </Button>
        )}
      </div>

      {cartItems.length === 0 ? (
        <Card className="text-center p-10">
          <p className="text-gray-500">Your cart is empty</p>
          <Button
            className="mt-4"
            onClick={() => router.push("/routes")}
          >
            Browse Routes
          </Button>
        </Card>
      ) : (
        <>
          <div className="space-y-4">
            {cartItems.map((item) => (
              <Card key={item.id} className="overflow-hidden">
                <CardHeader className="bg-gray-50">
                  <div className="flex justify-between items-start">
                    <div>
                      <CardTitle className="text-lg">
                        {item.route.busOperator} ({item.route.busType})
                      </CardTitle>
                      <p className="text-sm text-gray-600">
                        {item.route.origin} to {item.route.destination}
                      </p>
                    </div>
                    <Button
                      variant="ghost"
                      size="icon"
                      onClick={() => handleRemove(item.id)}
                      className="text-red-600 hover:bg-red-50"
                    >
                      <Trash2 className="w-5 h-5" />
                    </Button>
                  </div>
                </CardHeader>
                <CardContent className="pt-4">
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-2 text-sm">
                    <div>
                      <strong>Departure:</strong> {item.route.departureTime}
                    </div>
                    <div>
                      <strong>Arrival:</strong> {item.route.arrivalTime}
                    </div>
                    <div>
                      <strong>Seats:</strong> {item.seats}
                    </div>
                    <div>
                      <strong>Price:</strong> ₹{item.route.pricePerSeat}
                    </div>
                  </div>
                  <div className="mt-3 font-semibold text-right">
                    Total: ₹{item.route.pricePerSeat * item.seats}
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>

          <Card className="mt-6">
            <CardContent className="pt-6">
              <div className="flex justify-between text-xl font-bold">
                <span>Grand Total:</span>
                <span>₹{totalPrice}</span>
              </div>
              <Button
                className="w-full mt-4 bg-blue-600 hover:bg-blue-700"
                size="lg"
                onClick={() => router.push("/checkout")}
              >
                Proceed to Checkout
              </Button>
            </CardContent>
          </Card>
        </>
      )}
    </div>
  );
};

export default CartPage;