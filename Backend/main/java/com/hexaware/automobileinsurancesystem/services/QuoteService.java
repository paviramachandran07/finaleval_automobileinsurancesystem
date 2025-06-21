package com.hexaware.automobileinsurancesystem.services;

import java.util.List;

import com.hexaware.automobileinsurancesystem.entities.Proposal;
import com.hexaware.automobileinsurancesystem.entities.Quote;
import com.hexaware.automobileinsurancesystem.exceptions.QuoteNotFoundException;

public interface QuoteService {
    Quote createQuote(Quote quote);
    Quote createQuoteForProposal(Proposal proposal); // Added to match QuoteServiceImpl
    Quote updateQuote(Quote quote) throws QuoteNotFoundException;
    void deleteQuote(Long quoteId) throws QuoteNotFoundException;
    Quote getQuoteById(Long quoteId) throws QuoteNotFoundException;
    List<Quote> getAllQuotes();
    List<Quote> getQuotesByProposalId(Long proposalId);
}
