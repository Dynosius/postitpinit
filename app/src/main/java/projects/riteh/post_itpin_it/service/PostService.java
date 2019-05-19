package projects.riteh.post_itpin_it.service;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import projects.riteh.post_itpin_it.model.Post;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Singleton class. Use PostService.getInstance() to get the object reference.
 * Handles queries to FireStore database and returns data objects
 */
public class PostService extends Observable {
    private FirebaseFirestore firebaseFirestore;
    private String currentUser;
    private DocumentSnapshot mLastQueriedDocument;
    private static PostService postServiceInstance;
    private CollectionReference postsCollectionReference;
    private ArrayList<Post> pinnedPosts, unpinnedPosts;

    private PostService(){
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        initOnPostChangeListener();
        pinnedPosts = findPinnedPosts();
        unpinnedPosts = findUnpinnedPosts();
    }

    /**
     * Returns a singleton of type PostService
     * @return instance of FirebaseFirestore
     */
    public static PostService getInstance(){
        if(postServiceInstance == null){
            postServiceInstance = new PostService();
        }
        return postServiceInstance;
    }

    /**
     * Creates a new post and saves it to Firebase database with current user's ID as well as a unique firebase ID
     * @param post A post object
     */
    public void createPost(Post post){
        DocumentReference ref = firebaseFirestore.collection("posts").document();
        post.setUser_id(currentUser);
        post.setFirestore_id(ref.getId());
        // Insert to db
        ref.set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // TODO: Write success message
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO: Write failure message
            }
        });

    }

    public void deletePost(Post post){
        postsCollectionReference.document(post.getFirestore_id()).delete();
    }

    public void updatePost(Post post){
        String postId = post.getFirestore_id();
        // Creates a new post if the current post id is null (if we send update with new Post?)
        if (postId == null){
            postId = firebaseFirestore.collection("posts").document().getId();
        }
        DocumentReference ref = firebaseFirestore.collection("posts").document(postId);
        ref.set(post);
    }

    private void initOnPostChangeListener(){
        postsCollectionReference = firebaseFirestore.collection("posts");

        postsCollectionReference
                .whereEqualTo("reminder", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.w("Listen failed pinned", e);
                    return;
                }

                pinnedPosts = new ArrayList<>();
                for(QueryDocumentSnapshot doc: value){
                    pinnedPosts.add(doc.toObject(Post.class));
                }
                setChanged();
                notifyObservers(pinnedPosts);
            }
        });

        postsCollectionReference
                .whereEqualTo("reminder", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if(e != null){
                            Log.w("Listen failed unpinned", e);
                            return;
                        }
                        unpinnedPosts = new ArrayList<>();
                        for (QueryDocumentSnapshot doc: value){
                            unpinnedPosts.add(doc.toObject(Post.class));
                        }
                        setChanged();
                        notifyObservers(unpinnedPosts);
                    }
                });
    }

    public ArrayList<Post> getPinnedPosts() {
        return pinnedPosts;
    }

    public ArrayList<Post> getUnpinnedPosts() {
        return unpinnedPosts;
    }

    public Post findPostById(String id){
        CollectionReference postsReference = firebaseFirestore.collection("posts");
        Query postsQuery = postsReference.whereEqualTo("firebase_id", id);
        return getPosts(postsQuery).get(0);
    }

    /**
     * Returns all posts owned by the currently authenticated user
     * @return ArrayList<Post> object
     */
    public List<Post> findAllPosts(){
        final ArrayList<Post> posts;
        CollectionReference postsReference = firebaseFirestore.collection("posts");
        Query postsQuery = postsReference.whereEqualTo("user_id", currentUser);
        posts = getPosts(postsQuery);
        return posts;
    }
    /**
     * Finds pinned posts from firestore database
     * @return List<Post> of pinned posts
     */
    private ArrayList<Post> findPinnedPosts(){
        final ArrayList<Post> posts;
        CollectionReference postsReference = firebaseFirestore.collection("posts");
        Query postsQuery = postsReference
                .whereEqualTo("user_id", currentUser)
                .whereEqualTo("reminder", true);
        posts = getPosts(postsQuery);
        return posts;
    }

    /**
     * Finds unpinned posts from firestore database
     * @return List<Post> of unpinned posts
     */
    private ArrayList<Post> findUnpinnedPosts(){
        final ArrayList<Post> posts;
        CollectionReference postsReference = firebaseFirestore.collection("posts");
        Query postsQuery = postsReference
                .whereEqualTo("user_id", currentUser)
                .whereEqualTo("reminder", false);
        posts = getPosts(postsQuery);
        return posts;
    }

    // Internal method to avoid duplicate code
    private ArrayList<Post> getPosts(Query postsQuery){
        final ArrayList<Post> posts = new ArrayList<>();

        postsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        Post post = document.toObject(Post.class);
                        posts.add(post);
                    }
                    if (task.getResult().size() != 0){
                        mLastQueriedDocument = task.getResult().getDocuments()
                                .get(task.getResult().size() - 1);
                    }
                    // TODO: Recyclerview notifydatasetchanged
                }
            }
        });
        return posts;
    }
}
