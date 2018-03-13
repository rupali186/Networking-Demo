package com.example.rupali.jsonplaceholder;

/**
 * Created by RUPALI on 13-03-2018.
 */

public class Contract {
    static final String DATABASE_NAME="dbName";
    static final int VERSION=1;
    class ContractUser{
        static final String TABLE_NAME="userTable";
        static final String ID="userId";
        static final String NAME="userName";
        static final String USERNAME="userUsername";
        static final String EMAIL="userEmail";
    }
    class ContractPost{
        static final String TABLE_NAME="postTable";
        static final  String ID="postId";
        static final String USERID="userId";
        static final String TITLE="postTitle";
        static final String BODY="postBody";
    }
    class ContractComments{
        static final String TABLE_NAME="commentTable";
        static final String POSTID="postId";
        static final String ID="commentId";
        static final String NAME="commentName";
        static final  String EMAIL="commentEmail";
        static final String BODY="commentBody";
    }
}
