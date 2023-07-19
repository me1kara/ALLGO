package test.testspring.service;


import org.springframework.stereotype.Service;
import test.testspring.domain.Card;
import test.testspring.repository.CardRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class CardService {
    CardRepository cardRepository;
    public CardService(CardRepository cardRepository){
       this.cardRepository =  cardRepository;
    }
    public Card findCardById(Long cardId){
        return cardRepository.findById(cardId).orElseThrow();
    }

}
