package com.project.nonext.users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.nonext.MainActivity;
import com.project.nonext.R;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Home Fragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private android.widget.EditText SearchText;
    private RecyclerView RecyclerView,Wishlist;
    private ImageButton searchButton;
    private DatabaseReference mSearchDatabase;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private SearchAdapter searchAdapter;
    LinearLayout mLinearLayout;
    ArrayList<SearchModel> productsArrayList;
//    MyAdapter myAdapter;
    private String text;
    String title;
    FirestoreRecyclerAdapter adapter1;
    RecyclerView MyWishListRec;
    ArrayList<MyCart_Item_Model> MyWishListDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
   /* String UserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();*/
    MyWishList_Adapter WishListAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        SearchText = v.findViewById(R.id.search_field);
//        searchButton = v.findViewById(R.id.search_button);
        RecyclerView = v.findViewById(R.id.search_recyclerView);
        MyWishListRec = v.findViewById(R.id.WishlistRec);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SliderView slider = v.findViewById(R.id.slider_view);
        MaterialCardView electronics = v.findViewById(R.id.electronics);
        MaterialCardView fashion = v.findViewById(R.id.fashion);
        MaterialCardView mobile = v.findViewById(R.id.mobile);
        MaterialCardView homeAppliances = v.findViewById(R.id.homeAppliances);
        mLinearLayout = v.findViewById(R.id.MainLinearLayout);
        productsArrayList = new ArrayList<SearchModel>();
//        searchAdapter  = new SearchAdapter(productsArrayList);
//        RecyclerView.setAdapter(searchAdapter);

      /*  Wishlist.setHasFixedSize(true);
        Wishlist.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        */

        /*productsArrayList = new ArrayList<ProductsModel>();
        myAdapter  = new MyAdapter(productsArrayList);
        recyclerView.setAdapter(myAdapter);*/

        MyWishListRec.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));


        MyWishListDataList = new ArrayList<>();
        WishListAdapter = new MyWishList_Adapter(MyWishListDataList);
        MyWishListRec.setAdapter(WishListAdapter);
        if (MyWishListDataList != null) {

            db.collection("Wishlist")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {

                                MyCart_Item_Model obj = d.toObject(MyCart_Item_Model.class);
                                MyWishListDataList.add(obj);

                            }
                            WishListAdapter.notifyDataSetChanged();

                        }
                    });
        }

        Toolbar toolbarFragment = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setToolbar(toolbarFragment,"NoNext");


        int [] images={R.drawable.image_slider_1,R.drawable.image_slider_2,R.drawable.image_slider_3,R.drawable.image_slider_4,R.drawable.image_slider_5};
        String [] caption={"Slider 1","Slider 2","Slider 3","Slider 4","Slider 5"};
        SliderAdapter adapter=new SliderAdapter(images,caption);
        slider.setSliderAdapter(adapter);
        slider.startAutoCycle();



        electronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ElectronicsActivity.class);
                startActivity(intent);
            }
        });
        fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FashionActivity.class);
                startActivity(intent);
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MobileActivity.class));
            }
        });
        homeAppliances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AppliancesActivity.class));
            }
        });

        //Search Logic
//        Query query = fStore.collection("Products");
//        FirestoreRecyclerOptions<SearchModel> options = new FirestoreRecyclerOptions.Builder<SearchModel>()
//                .setQuery(query, SearchModel.class)
//                .build();
//        Log.d(TAG, String.valueOf(query));
//        mLinearLayout.setVisibility(View.GONE);
//        RecyclerView.setVisibility(View.VISIBLE);
//        adapter1 = new FirestoreRecyclerAdapter<SearchModel, SearchViewHolder>(options) {
//            @NonNull
//            @Override
//            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_rv_item,parent,false);
//                return new SearchViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull SearchModel model) {
//                holder.title.setText(model.getTitle());
////                Picasso.get().load(productsArrayList.get(position).getUrl())
////                        .error(R.drawable.logo).into(holder.img);
//            }
//        };
//         RecyclerView.setHasFixedSize(true);
//         RecyclerView.setAdapter(adapter1);
//        searchAdapter = new SearchAdapter(options);
//        RecyclerView.setAdapter(searchAdapter);
/*
        SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable s) {
                mLinearLayout.setVisibility(View.GONE);
                RecyclerView.setVisibility(View.VISIBLE);
                Log.d(TAG, "Searchbox has changed to: " + s.toString());
//                if (s.toString().isEmpty()) {
//                    query = fStore.collection("products")
//                            .orderBy("createdTime", Query.Direction.ASCENDING);
//                }else {
                    Query query = fStore.collection("Products")
                            .whereEqualTo("title", s.toString());
//                }
                Log.d(TAG, String.valueOf(query));
                FirestoreRecyclerOptions<SearchModel> options = new FirestoreRecyclerOptions.Builder<SearchModel>()
                        .setQuery(query, SearchModel.class)
                        .build();
                Log.d(TAG, String.valueOf(options));

//                searchAdapter = new SearchAdapter(options);
//                RecyclerView.setAdapter(searchAdapter);
                RecyclerView.setHasFixedSize(true);
                RecyclerView.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
            }
        });
*/
//        text = String.valueOf(SearchText.getText());
       /* SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = SearchText.getText().toString();
//                DocumentReference df = fStore.collection("Products").document();
                fStore.collection("Products").whereEqualTo("title",text)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
//                            if(progressDialog.isShowing())
//                               progressDialog.dismiss();
//                            electronicsProgressBar.setVisibility(View.GONE);
                            Log.e("Firestore Error",error.getMessage());
                            return;
                        }
                        assert value != null;
                        Log.d(TAG,SearchText.getText().toString());
                        mLinearLayout.setVisibility(View.GONE);
                        RecyclerView.setVisibility(View.VISIBLE);
                        for(DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                        productsArrayList.add(dc.getDocument().toObject(SearchModel.class));
//                                if(productsArrayList.contains(true)){
//                                    productsArrayList.add(dc.getDocument().toObject(SearchModel.class));
//                                }
                                if(dc.getDealDisc)

                            }
                            RecyclerView.setAdapter(searchAdapter);
                            searchAdapter.notifyDataSetChanged();
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
//                            electronicsProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });*/






        Query query = fStore.collection("Products");
        FirestoreRecyclerOptions<SearchModel> options = new FirestoreRecyclerOptions.Builder<SearchModel>()
                .setQuery(query, SearchModel.class)
                .build();

        searchAdapter =new SearchAdapter(options);
        RecyclerView.setAdapter(searchAdapter);

        EditText searchBox = v.findViewById(R.id.search_field);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                mLinearLayout.setVisibility(View.GONE);

                Log.d(TAG, "Searchbox has changed to: " + s.toString());
                Query query;
                if (s.toString().isEmpty()) {
                    slider.setVisibility(View.VISIBLE);
                    electronics.setVisibility(View.VISIBLE);
                    fashion.setVisibility(View.VISIBLE);
                    mobile.setVisibility(View.VISIBLE);
                    homeAppliances.setVisibility(View.VISIBLE);
                    RecyclerView.setVisibility(View.GONE);
                    query = fStore.collection("Products");
                } else {
                    slider.setVisibility(View.GONE);
                    electronics.setVisibility(View.GONE);
                    fashion.setVisibility(View.GONE);
                    mobile.setVisibility(View.GONE);
                    homeAppliances.setVisibility(View.GONE);
                    RecyclerView.setVisibility(View.VISIBLE);
                    query = fStore.collection("Products")
                            .whereEqualTo("title", s.toString());
                }
                FirestoreRecyclerOptions<SearchModel> options = new FirestoreRecyclerOptions.Builder<SearchModel>()
                        .setQuery(query, SearchModel.class)
                        .build();
                searchAdapter.updateOptions(options);
                searchAdapter.notifyDataSetChanged();
            }
        });








        return v;
    }

//    private class SearchViewHolder extends RecyclerView.ViewHolder{
//        private TextView title;
//        private ImageView img;
//        public SearchViewHolder(@NonNull View itemView) {
//            super(itemView);
//            img = itemView.findViewById(R.id.searchProductImageView);
//            title = itemView.findViewById(R.id.search_product);
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
        searchAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        searchAdapter.startListening();
    }
}