package ateam.foodr;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class ChildEventListenerBuilder implements ChildEventListener {

    public interface ChildHandlerMethod {
        void run(DataSnapshot snapshot, String prevChild);
    }

    public interface ChildRemovedHandlerMethod {
        void run(DataSnapshot snapshot);
    }

    public interface DatabaseErrorHandlerMethod{
        void run(DatabaseError error);
    }

    private ChildHandlerMethod childAdded;
    private ChildHandlerMethod childChanged;
    private ChildHandlerMethod childMoved;
    private ChildRemovedHandlerMethod childRemoved;
    private DatabaseErrorHandlerMethod cancelled;

    public ChildEventListenerBuilder() {

        // Default all the handlers to empty methods
        childAdded = (s, c) -> {};
        childChanged = (s, c) -> {};
        childMoved = (s, c) -> {};
        childRemoved = (s) -> {};

        // Except for cancelled!  Instead, we default it to
        // throw an exception!
        cancelled = (e) -> { throw new RuntimeException(e.getMessage()); };
    }

    /* Builder methods */

    /** Sets the onChildAdded event to be handled by the given lambda expression.
     *  Returns this object so you can use method chaining.
     * @param m
     * @return
     */
    public ChildEventListenerBuilder whenChildIsAdded(ChildHandlerMethod m){
        childAdded = m;
        return this;
    }

    /** Sets the onChildChanged event to be handled by the given lambda expression.
     *  Returns this object so you can use method chaining.
     * @param m
     * @return
     */
    public ChildEventListenerBuilder whenChildIsChanged(ChildHandlerMethod m){
        childChanged = m;
        return this;
    }

    /** Sets the onChildMoved event to be handled by the given lambda expression.
     *  Returns this object so you can use method chaining.
     * @param m
     * @return
     */
    public ChildEventListenerBuilder whenChildIsMoved(ChildHandlerMethod m){
        childMoved = m;
        return this;
    }

    /** Sets the onChildRemoved event to be handled by the given lambda expression.
     *  Returns this object so you can use method chaining.
     * @param m
     * @return
     */
    public ChildEventListenerBuilder whenChildIsRemoved(ChildRemovedHandlerMethod m){
        childRemoved = m;
        return this;
    }

    /** Sets the onCancelled event to be handled by the given lambda expression.
     *  Returns this object so you can use method chaining.
     * @param m
     * @return
     */
    public ChildEventListenerBuilder whenCancelled(DatabaseErrorHandlerMethod m){
        cancelled = m;
        return this;
    }


    /* Interface implementation.  They all just forward the method calls to the lambdas you provided */

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        childAdded.run(dataSnapshot, s);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        childChanged.run(dataSnapshot, s);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        childMoved.run(dataSnapshot, s);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        childRemoved.run(dataSnapshot);
    }

    @Override
    public void onCancelled(DatabaseError error) {
        cancelled.run(error);
    }
}
