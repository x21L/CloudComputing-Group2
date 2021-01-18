import React, { useState, useEffect } from "react";
import "../App.css";
import Header from "./Header";
import Book from "./Book";
import ShopBook from "./ShopBook";
import Search from "./Search";


const BOOK_API_URL = "http://35.193.104.75:8080/api";
const SHOP_API_URL = "http://35.239.83.61/shopping-cart-1.0/ShoppingCart";

const App = () => {
  const [loading, setLoading] = useState(true);
  const [books, setBooks] = useState([]);
  const [errorMessage, setErrorMessage] = useState(null);
  const [searchValueString, setSearchValueString] = useState('All Books');
  const [shopOpen, setShopOpen] = useState(false);
  const [shopLoading, setShopLoading] = useState(true);
  const [shopErrorMessage, setShopErrorMessage] = useState(null);
  const [shopBooks, setShopBooks] = useState([]);

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
    .catch(error => {
        console.log('Search-Error: ' + error);
        setErrorMessage('' + error);
      });
  }, []);

  const search = searchValue => {
    setLoading(true);
    setErrorMessage(null);

    if (searchValue === "")
      setSearchValueString("All Books");
    else
      setSearchValueString(searchValue);

    searchValue = '*' + searchValue + '*'

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
      .catch(error => {
        console.log('Search-Error: ' + error);
        setErrorMessage('' + error);
      });
  };

  const toggleShop = () => {
    setShopOpen(!shopOpen);
    refreshShopCart();
  }

  const buyBook = (id) => {
    console.log('BUY THE BOOK ID: ', id)
    fetch(SHOP_API_URL + `?action=insert&user=1&IBAN=${id}`)
      .then(response => {
        toggleShop()
        return response.json()
      })
      .catch(error => console.log(error));
  }

  const removeShopBook = (id) => {
    fetch(SHOP_API_URL + `?action=delete&user=1&IBAN=${id}`)
      .then(response => response.json())
      .catch(error => console.log(error));

    refreshShopCart();
  }
  
  function refreshShopCart() {
    setShopLoading(true);
    setShopErrorMessage(null);

    fetch(SHOP_API_URL + `?action=getAll`)
      .then(response => {
        if (response.status === 204) { /* NO CONTENT */
          setShopErrorMessage("NO CONTENT CAN BE FOUND!");
          setShopLoading(false);
          throw new Error('No Content');
        } else {
          return response.json();
        }
      })
      .then(jsonResponse => {
        setShopBooks(jsonResponse);
        setShopLoading(false);
      })
      .catch(error => {
        console.log('Search-Error: ' + error);
        setShopErrorMessage('' + error);
      });
  }

  return (
    <div className="App">
      <div>
      <Header text="Book-Store"/>
      <button id={"shopButton_" + shopOpen} onClick={toggleShop}></button>
    </div>
    {!shopOpen ? (
      <div  className="main">
        <Search search={search} />
        <p className="App-intro">Searchresults for: {searchValueString}</p>
        <div className="Book">
          {loading && !errorMessage ? (
          <span>loading...</span>
          ) : errorMessage ? (
          <div className="errorMessage">{errorMessage}</div>
          ) : (
            books.map((book, index) => (
              <Book key={`${index}-${book.Title}`} book={book} add={buyBook}/>
            ))
          )}
        </div>
      </div>
    ) : (
      <div  className="shopcart">
        <h2>Shopping cart:</h2>
        <div className="Shop">
          {shopLoading && !shopErrorMessage ? (
            <span>loading...</span>
          ) : shopErrorMessage ? (
          <div className="errorMessage">{shopErrorMessage}</div>
          ) : (
            shopBooks.map((shopBook, index) => (
              books.map((book, b_index) => {
                if (shopBook.book === book.id)
                  return <ShopBook key={`${index}-${shopBook.book}`} shopBook={book} remove={removeShopBook}/>
                else return null
              })
            ))
          )}
        </div>
      </div>
    )}
    </div>
  );
};


export default App;