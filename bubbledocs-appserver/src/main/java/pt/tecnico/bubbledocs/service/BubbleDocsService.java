package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;

// add needed import declarations
import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;

public abstract class BubbleDocsService {

    @Atomic
    public final void execute() throws BubbleDocsException {
        dispatch();
    }

    protected abstract void dispatch() throws BubbleDocsException;
}
