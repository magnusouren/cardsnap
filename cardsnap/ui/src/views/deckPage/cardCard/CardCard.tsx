import { FC } from 'react';
import { CardContent } from './cardContent';
import { Card } from '../../../helpers/mockData';
import './cardCard.css';

interface CardProps {
  card: Card;
  displayAnswer: boolean;
}

/**
 * Card that renders the content of a given card.
 * @param card Card to be shown
 * @param displayAnswer boolean hide/show the answer
 */
export const CardCard: FC<CardProps> = ({ card, displayAnswer }) => (
  <div className={`card ${displayAnswer ? 'flipped' : ''} relative w-full h-64`}>
    <div className="content absolute w-full h-full transition-all duration-300 shadow rounded">
      <CardContent type="Question" text={card.question} />
      <CardContent type="Answer" text={card.answer} />
    </div>
  </div>
);
