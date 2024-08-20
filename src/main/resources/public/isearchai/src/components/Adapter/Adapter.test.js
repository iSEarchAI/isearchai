import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import Adapter from './Adapter';

describe('<Adapter />', () => {
  test('it should mount', () => {
    render(<Adapter />);

    const Adapter = screen.getByTestId('Adapter');

    expect(Adapter).toBeInTheDocument();
  });
});
