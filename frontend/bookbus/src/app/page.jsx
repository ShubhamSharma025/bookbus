"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";

export default function Home() {
  const router = useRouter();
  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");

  const handleSearch = () => {
    if (!from || !to) {
      alert("Please enter both departure and destination cities.");
      return;
    }
    // Only pass 'from' and 'to', ignore date
    const query = new URLSearchParams({ from, to }).toString();
    router.push(`/routes?${query}`);
  };

  return (
    <main className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-r from-blue-500 to-indigo-600 text-white">
      
      {/* Hero Section */}
      <div className="text-center px-6">
        <h1 className="text-5xl font-extrabold mb-4">Welcome to NeoBus</h1>
        <p className="text-lg mb-8">
          Book your bus tickets easily, track your bookings, and enjoy a smooth travel experience.
        </p>

        {/* CTA Buttons */}
        <div className="flex gap-4 justify-center">
          <button
            type="button"
            onClick={() => router.push("/auth/login")}
            className="bg-white text-blue-600 px-6 py-2 rounded-lg font-semibold shadow hover:bg-gray-200 transition"
          >
            Login
          </button>
          <button
            type="button"
            onClick={() => router.push("/auth/signup")}
            className="bg-yellow-400 text-gray-900 px-6 py-2 rounded-lg font-semibold shadow hover:bg-yellow-300 transition"
          >
            Sign Up
          </button>
        </div>
      </div>

      {/* Quick Search Section */}
      <div className="mt-16 bg-white text-gray-900 rounded-2xl shadow-xl p-8 max-w-3xl w-full">
        <h2 className="text-2xl font-bold mb-4 text-center">Quick Search</h2>
        <div className="flex flex-col md:flex-row gap-4">
          <input
            type="text"
            placeholder="Enter departure city"
            value={from}
            onChange={(e) => setFrom(e.target.value)}
            className="flex-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <input
            type="text"
            placeholder="Enter destination city"
            value={to}
            onChange={(e) => setTo(e.target.value)}
            className="flex-1 p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <button
            type="button"
            onClick={handleSearch}
            className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition"
          >
            Search
          </button>
        </div>
      </div>

      {/* Optional Features Section */}
      <div className="mt-12 flex flex-col md:flex-row gap-6 justify-center text-center">
        {/* Bookings */}
        <div className="bg-white text-gray-900 rounded-xl shadow-xl p-6 w-64 hover:scale-105 transition">
          <h3 className="font-bold text-xl mb-2">View Bookings</h3>
          <p className="mb-4">Check all your past and upcoming bookings</p>
          <button
            type="button"
            onClick={() => router.push("/bookings")}
            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition"
          >
            My Bookings
          </button>
        </div>

        {/* Cart */}
        <div className="bg-white text-gray-900 rounded-xl shadow-xl p-6 w-64 hover:scale-105 transition">
          <h3 className="font-bold text-xl mb-2">Cart</h3>
          <p className="mb-4">View selected routes and proceed to booking</p>
          <button
            type="button"
            onClick={() => router.push("/cart")}
            className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition"
          >
            Go to Cart
          </button>
        </div>
      </div>
    </main>
  );
}
