git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/teshlya/reddit.git
git push -u origin master


Hello. How remove item from recyclerview?

`     @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                articles.remove(position);
                notifyDataSetChanged();
            }`

If this code, then I have white empty space. Video - https://drive.google.com/file/d/1nnJ3AXXailegOfxaYb-cykoOs72A3oZ0/view

`     @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                articles.remove(position);
                notifyItemRemoved(position);
            }`

If this code, then I have error on position - 0. Video - https://drive.google.com/file/d/1nnJ3AXXailegOfxaYb-cykoOs72A3oZ0/view

`java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid item position 0(offset:-1).state:25 me.saket.inboxrecyclerview.InboxRecyclerView{48c7ea0 VFED..... .F....ID 0,0-1440,2448 #7f090038 app:id/community_recycler_view}, adapter:teshlya.com.reddit.adapter.CommunityAdapter@239767f, layout:androidx.recyclerview.widget.LinearLayoutManager@9cc564c, context:teshlya.com.reddit.screen.ArticleActivity@9771922
        at androidx.recyclerview.widget.RecyclerView$Recycler.tryGetViewHolderForPositionByDeadline(RecyclerView.java:5923)
        at androidx.recyclerview.widget.RecyclerView$Recycler.getViewForPosition(RecyclerView.java:5858)
        at androidx.recyclerview.widget.RecyclerView$Recycler.getViewForPosition(RecyclerView.java:5854)
        at androidx.recyclerview.widget.LinearLayoutManager$LayoutState.next(LinearLayoutManager.java:2230)
        at androidx.recyclerview.widget.LinearLayoutManager.layoutChunk(LinearLayoutManager.java:1557)
        at androidx.recyclerview.widget.LinearLayoutManager.fill(LinearLayoutManager.java:1517)
        at androidx.recyclerview.widget.LinearLayoutManager.onLayoutChildren(LinearLayoutManager.java:622)
        at androidx.recyclerview.widget.RecyclerView.dispatchLayoutStep1(RecyclerView.java:3875)`












@Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) { int position = viewHolder.getAdapterPosition(); articles.remove(position); notifyDataSetChanged(); }

This work good with RecyclerView, but with InboxRecyclerView I have empty white space - https://drive.google.com/file/d/1nnJ3AXXailegOfxaYb-cykoOs72A3oZ0/view