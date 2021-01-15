import React, { useState, useEffect } from "react";
import "../App.css";
import Header from "./Header";
import Book from "./Book";
import Search from "./Search";


const BOOK_API_URL = "https://10.8.13.106/api"; // https://www.omdbapi.com/?s=man&apikey=4a3b711b


const App = () => {
  const [loading, setLoading] = useState(true);
  const [books, setBooks] = useState([]);
  const [errorMessage, setErrorMessage] = useState(null);
  const [searchValueString, setSearchValueString] = useState('All Books');

    useEffect(() => {
    fetch(BOOK_API_URL + `/search?query=*`)
      .then(response => {
        console.log('got status: ', response.status)
        if (response.status === 204) { /* NO CONTENT */
          setErrorMessage("NO CONTENT CAN BE FOUND!");
          setLoading(false);
          throw new Error('No Content');
        } else {
          return response.json();
        }
      })
      .then(jsonResponse => {
        setBooks(jsonResponse);
        setLoading(false);
      })
      .catch(error => console.log(error));
    }, []);

    const search = searchValue => {
    setLoading(true);
    setErrorMessage(null);
    setSearchValueString(searchValue);

    fetch(BOOK_API_URL + `/search?query=${searchValue}`)
      .then(response => {
        if (response.status === 204) { /* NO CONTENT */
          setErrorMessage("NO CONTENT CAN BE FOUND!");
          setLoading(false);
          throw new Error('No Content');
        } else {
          return response.json();
        }
      })
      .then(jsonResponse => {
        setBooks(jsonResponse);
        setLoading(false);
      })
      .catch(error => console.log(error));
  	};

    
    return (
     <div className="App">
      <Header text="Book-Store" />
      <Search search={search} />
      <p className="App-intro">V5 Searchresults for: {searchValueString}</p>
      <div className="Book">
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