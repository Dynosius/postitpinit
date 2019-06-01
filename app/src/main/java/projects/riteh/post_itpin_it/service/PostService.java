package projects.riteh.post_itpin_it.service;

import android.arch.lifecycle.MutableLiveData;
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
    private MutableLiveData<ArrayList<Post>> pinnedPosts, unpinnedPosts;


    private PostService(){
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        initOnPostChangeListener();
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
        findPinnedPosts();
        findUnpinnedPosts();
        postsCollectionReference
                .whereEqualTo("reminder", true)
                .whereEqualTo("user_id", currentUser)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.w("Listen failed pinned", e);
                    return;
                }

                ArrayList<Post> current = new ArrayList<>();
                for(QueryDocumentSnapshot doc: value){
                    current.add(doc.toObject(Post.class));
                }
                pinnedPosts.postValue(current);
            }
        });

        postsCollectionReference
                .whereEqualTo("reminder", false)
                .whereEqualTo("user_id", currentUser)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if(e != null){
                            Log.w("Listen failed unpinned", e);
                            return;
                        }
                        ArrayList<Post> current = new ArrayList<>();
                        for (QueryDocumentSnapshot doc: value){
                            current.add(doc.toObject(Post.class));
                        }
                        unpinnedPosts.postValue(current);
                    }
                });
    }

    /**
     * Finds pinned posts from firestore database
     * @return List<Post> of pinned posts
     */
    private void findPinnedPosts(){
        CollectionReference postsReference = firebaseFirestore.collection("posts");
        Query postsQuery = postsReference
                .whereEqualTo("user_id", currentUser)
                .whereEqualTo("reminder", true);
        pinnedPosts = getPosts(postsQuery);
    }

    /**
     * Finds unpinned posts from firestore database
     * @return List<Post> of unpinned posts
     */
    private void findUnpinnedPosts(){
        CollectionReference postsReference = firebaseFirestore.collection("posts");
        Query postsQuery = postsReference
                .whereEqualTo("user_id", currentUser)
                .whereEqualTo("reminder", false);
        unpinnedPosts = getPosts(postsQuery);
    }

    // Internal method to avoid duplicate code
    private MutableLiveData<ArrayList<Post>> getPosts(Query postsQuery){
        final ArrayList<Post> posts = new ArrayList<>();
        final MutableLiveData<ArrayList<Post>> liveDataPost = new MutableLiveData<>();

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

                    liveDataPost.postValue(posts);
                }
            }
        });
        return liveDataPost;
    }

    public MutableLiveData<ArrayList<Post>> getPinnedPosts() {
        return pinnedPosts;
    }

    public MutableLiveData<ArrayList<Post>> getUnpinnedPosts() {
        return unpinnedPosts;
    }
}
