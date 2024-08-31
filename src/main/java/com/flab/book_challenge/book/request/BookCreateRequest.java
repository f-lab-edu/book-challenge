package com.flab.book_challenge.book.request;

public record BookCreateRequest(String isbn, String name, int pageCount) {

}
