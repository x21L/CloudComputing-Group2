import React, { useState, useEffect } from "react";
import "../App.css";
import Header from "./Header";
import Book from "./Book";
import Search from "./Search";


const BOOK_API_URL = "http://127.0.0.1:8080/api"; // https://www.omdbapi.com/?s=man&apikey=4a3b711b


const App = () => {
  const [loading, setLoading] = useState(true);
  const [books, setBooks] = useState([]);
  const [errorMessage, setErrorMessage] = useState(null);

    useEffect(() => {
    fetch(BOOK_API_URL + `/search?query=*`)
      .then(response => response.json())
      .then(jsonResponse => {
        setBooks(jsonResponse.Search);
        setLoading(false);
      });
  }, []);

    const search = searchValue => {
    setLoading(true);
    setErrorMessage(null);

    fetch(BOOK_API_URL + `/search?query=${searchValue}`)
      .then(response => response.json())
      .then(jsonResponse => {
        if (jsonResponse.Response === "True") {
          setBooks(jsonResponse.Search);
          setLoading(false);
        } else {
          setErrorMessage(jsonResponse.Error);
          setLoading(false);
        }
      });
  	};

    
    return (
     <div className="App">
      <Header text="Book-Store" />
      <Search search={search} />
      <p className="App-intro">Search for Books</p>
      <div className="books">
        {loading && !errorMessage ? (
         <span>loading...</span>
         ) : errorMessage ? (
          <div className="errorMessage">{errorMessage}</div>
        ) : (
          books.map((book, index) => (
            <Book key={`${index}-${book.Title}`} book={book} />
          ))
        )}
      </div>
    </div>
  );
};


export default App;