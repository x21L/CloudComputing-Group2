import React from "react";

const ShopBook = (props) => {

  const removeBook = () => {
    props.remove(props.shopBook.id);
  }

  return (
    <div className="shopBook">
      <div>
        <img
          id="shopPic"
          width="100"
          alt={`Title: test`}
          src={"https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Closed_Book_Icon.svg/1200px-Closed_Book_Icon.svg.png"}
        />
      </div>
      <div className='shop_info'>
        <p>ID: {props.shopBook.id}</p>
        <p>Name: {props.shopBook.name}</p>
        <p><button id="removeBook" onClick={removeBook}></button></p>
      </div>
      <hr/>
    </div>
  );
};


export default ShopBook;