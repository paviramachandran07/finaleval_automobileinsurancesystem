package com.hexaware.automobileinsurancesystem.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.hexaware.automobileinsurancesystem.entities.Quote;
import com.hexaware.automobileinsurancesystem.entities.Proposal;
import com.hexaware.automobileinsurancesystem.exceptions.QuoteNotFoundException;
import com.hexaware.automobileinsurancesystem.repositories.QuoteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class QuoteServiceImplTest {

    @Mock private QuoteRepository quoteRepository;

    @InjectMocks private QuoteServiceImpl quoteService;

    private Quote quote;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        quote = new Quote();
        quote.setQuoteId(1L);
        quote.setProposal(new Proposal());
    }

    @Test
    void testCreateQuote() {
        when(quoteRepository.save(quote)).thenReturn(quote);
        Quote saved = quoteService.createQuote(quote);
        assertEquals(quote, saved);
    }

    @Test
    void testUpdateQuote() throws QuoteNotFoundException {
        when(quoteRepository.findById(1L)).thenReturn(Optional.of(quote));
        when(quoteRepository.save(quote)).thenReturn(quote);
        Quote updated = quoteService.updateQuote(quote);
        assertEquals(quote, updated);
    }

    @Test
    void testDeleteQuoteById() throws QuoteNotFoundException {
        when(quoteRepository.findById(1L)).thenReturn(Optional.of(quote));
        quoteService.deleteQuote(1L);
        verify(quoteRepository).delete(quote);
    }

    @Test
    void testGetQuoteById_Success() throws QuoteNotFoundException {
        when(quoteRepository.findById(1L)).thenReturn(Optional.of(quote));
        assertEquals(quote, quoteService.getQuoteById(1L));
    }

    @Test
    void testGetQuoteById_NotFound() {
        when(quoteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(QuoteNotFoundException.class, () -> quoteService.getQuoteById(1L));
    }

    @Test
    void testGetAllQuotes() {
        when(quoteRepository.findAll()).thenReturn(List.of(quote));
        assertEquals(1, quoteService.getAllQuotes().size());
    }

    @Test
    void testGetQuotesByProposalId() {
        when(quoteRepository.findByProposal_ProposalId(1L)).thenReturn(List.of(quote));
        assertEquals(1, quoteService.getQuotesByProposalId(1L).size());
    }
}

