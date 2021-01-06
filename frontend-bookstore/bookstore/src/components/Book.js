import React from "react";

const DEFAULT_PLACEHOLDER_IMAGE =
  "https://m.media-amazon.com/images/M/MV5BMTczNTI2ODUwOF5BMl5BanBnXkFtZTcwMTU0NTIzMw@@._V1_SX300.jpg";


const Book = ({ book }) => {
  const poster =
    book.Poster === "N/A" ? DEFAULT_PLACEHOLDER_IMAGE : book.Poster;
  return (
    <div className="book">
      <h2>{book.name}</h2>
      <div>
        <img
          width="200"
          alt={`The book titled: ${book.name}`}
          src={poster}
        />
      </div>
      <p>({book.description})</p>
      <p>({book.author})</p>
    </div>
  );
};


export default Book;