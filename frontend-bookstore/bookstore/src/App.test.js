import { render, screen } from '@testing-library/react';
import App from './App';

test('renders book-store', () => {
  render(<App />);
  const linkElement = screen.getByText(/Book-Store/i);
  expect(linkElement).toBeInTheDocument();
});
