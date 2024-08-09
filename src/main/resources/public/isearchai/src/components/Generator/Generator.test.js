import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import Generator from './Generator';

describe('<Generator />', () => {
  test('it should mount', () => {
    render(<Generator />);

    const Generator = screen.getByTestId('Generator');

    expect(Generator).toBeInTheDocument();
  });
});