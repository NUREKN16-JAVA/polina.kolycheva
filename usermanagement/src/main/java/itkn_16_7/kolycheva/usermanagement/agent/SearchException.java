package itkn_16_7.kolycheva.usermanagement.agent;

import itkn_16_7.kolycheva.usermanagement.db.DatabaseException;

public class SearchException extends Exception {

    public SearchException(DatabaseException e) {
        super(e);
    }
}