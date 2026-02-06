import { useState } from "react";
import "../style/FlightSearch.css";


const BASE_URL = "http://localhost:8080";

function FlightSearch() {
  const [fromText, setFromText] = useState("");
  const [toText, setToText] = useState("");
  const [fromCode, setFromCode] = useState("");
  const [toCode, setToCode] = useState("");
  const [date, setDate] = useState("");

  const [fromOptions, setFromOptions] = useState([]);
  const [toOptions, setToOptions] = useState([]);
  const [flights, setFlights] = useState([]);
  const [loading, setLoading] = useState(false);

  const searchLocations = async (keyword, type) => {
    if (keyword.length < 2) return;

    const res = await fetch(
      `${BASE_URL}/api/flights/locations?keyword=${keyword}`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );

    const data = await res.json();

    type === "from"
      ? setFromOptions(data.data || [])
      : setToOptions(data.data || []);
  };

  const searchFlights = async () => {
    if (!fromCode || !toCode || !date) return;

    setLoading(true);

    const res = await fetch(
      `${BASE_URL}/api/flights/search?from=${fromCode}&to=${toCode}&date=${date}`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );

    const data = await res.json();
    setFlights(data.data || []);
    setLoading(false);
  };

  return (
    <div className="flight-page">
      <h1 className="title">✈️ Flight Search</h1>

      {/* SEARCH BAR */}
      <div className="search-box">
        <div className="field">
          <input
            placeholder="From"
            value={fromText}
            onChange={(e) => {
              setFromText(e.target.value);
              searchLocations(e.target.value, "from");
            }}
          />
          {fromOptions.length > 0 && (
            <div className="dropdown">
              {fromOptions.map((item) => (
                <div
                  key={item.id}
                  onClick={() => {
                    setFromText(`${item.name} (${item.iataCode})`);
                    setFromCode(item.iataCode);
                    setFromOptions([]);
                  }}
                >
                  {item.name} ({item.iataCode})
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="field">
          <input
            placeholder="To"
            value={toText}
            onChange={(e) => {
              setToText(e.target.value);
              searchLocations(e.target.value, "to");
            }}
          />
          {toOptions.length > 0 && (
            <div className="dropdown">
              {toOptions.map((item) => (
                <div
                  key={item.id}
                  onClick={() => {
                    setToText(`${item.name} (${item.iataCode})`);
                    setToCode(item.iataCode);
                    setToOptions([]);
                  }}
                >
                  {item.name} ({item.iataCode})
                </div>
              ))}
            </div>
          )}
        </div>

        <input
          type="date"
          className="date-input"
          value={date}
          onChange={(e) => setDate(e.target.value)}
        />

        <button onClick={searchFlights}>Search</button>
      </div>

      {/* RESULTS */}
      {loading && <p className="loading">Searching flights...</p>}

      <div className="results">
        {flights.map((flight, i) => {
          const segments = flight.itineraries[0].segments;

          const departure = new Date(
            segments[0].departure.at
          ).toLocaleTimeString("en-IN", { hour: "2-digit", minute: "2-digit" });

          const arrival = new Date(
            segments[segments.length - 1].arrival.at
          ).toLocaleTimeString("en-IN", { hour: "2-digit", minute: "2-digit" });

          const price = new Intl.NumberFormat("en-IN", {
            style: "currency",
            currency: "INR",
          }).format(flight.price.total);

          return (
            <div className="flight-card" key={i}>
              <div>
                <p className="route">
                  {segments[0].departure.iataCode} →{" "}
                  {segments[segments.length - 1].arrival.iataCode}
                </p>
                <p className="time">
                  {departure} – {arrival}
                </p>
                <p className="stops">
                  {segments.length - 1 === 0
                    ? "Non-stop"
                    : `${segments.length - 1} Stop`}
                </p>
              </div>

              <div className="price">
                <p>{price}</p>
                <button className="book-btn">Book</button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default FlightSearch;
