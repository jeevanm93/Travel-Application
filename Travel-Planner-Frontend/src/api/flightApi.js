const BASE_URL = "http://localhost:8080";

export async function searchFlights(from, to, date) {
  const response = await fetch(
    `${BASE_URL}/api/flights/search?from=${from}&to=${to}&date=${date}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`, // if protected
      },
    }
  );

  if (!response.ok) {
    throw new Error("Failed to fetch flights");
  }

  return response.json();
}
