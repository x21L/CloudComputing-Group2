import React from "react";

const Book = (props) => {

  const buyBook = () => {
    props.add(props.book.id);
  }

  return (
    <div className="book">
      <h2>{props.book.name}</h2>
      <div>
        <img
          width="100"
          alt={`Title: ${props.book.name}`}
          src={"https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Closed_Book_Icon.svg/1200px-Closed_Book_Icon.svg.png"}
        />
      </div>
      <p>Description: {props.book.description}</p>
      <p>Author: {props.book.author}</p>
      <p><button id="buyBook" onClick={buyBook}></button></p>
      <hr/>
    </div>
  );
};


export default Book;