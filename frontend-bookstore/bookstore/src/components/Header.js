import React from "react";

const Header = (props) => {
  return (
    <header className="App-header">
      <h2>{props.text}</h2>
      <title>Beispiel</title>
    </header>
  );
};

export default Header;