import React from "react";

const DEFAULT_PLACEHOLDER_IMAGE =
  "https://m.media-amazon.com/images/M/MV5BMTczNTI2ODUwOF5BMl5BanBnXkFtZTcwMTU0NTIzMw@@._V1_SX300.jpg";


const Book = ({ book }) => {
  return (
    <div className="book">
      <h2>{book.name}</h2>
      <div>
        <img
          width="100"
          alt={`Title: ${book.name}`}
          src={"https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Closed_Book_Icon.svg/1200px-Closed_Book_Icon.svg.png"}
        />
      </div>
      <p>Description: {book.description}</p>
      <p>Author: {book.author}</p>
      <hr/>
    </div>
  );
};


export default Book;