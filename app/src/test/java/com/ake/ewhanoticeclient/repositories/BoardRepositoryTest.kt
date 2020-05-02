package com.ake.ewhanoticeclient.repositories

import android.content.SharedPreferences
import com.ake.ewhanoticeclient.database.BoardDatabaseDao
import com.ake.ewhanoticeclient.domain.Board
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(FirebaseMessaging::class)
class BoardRepositoryTest {

    private lateinit var firebaseMessaging: FirebaseMessaging
    private var subscribedTopics = mutableSetOf<String>()  // Firebase Messaging
    private var mockBoardsDataString = ""  // SharedPreferences

    private lateinit var boardRepository: BoardRepository

    @Before
    fun init(){
        mockFirebaseMessaging()
        mockRepository()
    }

    private fun mockFirebaseMessaging(){
        PowerMockito.mockStatic(FirebaseMessaging::class.java)
        val task = mock(Task::class.java)

        firebaseMessaging = mock(FirebaseMessaging::class.java)
        `when`(firebaseMessaging.subscribeToTopic(anyString())).then {
            subscribedTopics.add(it.arguments[0] as String)
            task
        }
        `when`(firebaseMessaging.unsubscribeFromTopic(anyString())).then {
            subscribedTopics.remove(it.arguments[0] as String)
            task
        }

        `when`(FirebaseMessaging.getInstance()).thenReturn(firebaseMessaging)
    }

    private fun mockRepository(){
        val dao = mock(BoardDatabaseDao::class.java)
        val prefs = mock(SharedPreferences::class.java)
        val editor = mock(SharedPreferences.Editor::class.java)

        `when`(prefs.edit()).thenReturn(editor)
        `when`(prefs.getString(anyString(), anyString())).then {
            mockBoardsDataString
        }
        `when`(editor.putString(anyString(), anyString())).then {
            mockBoardsDataString = it.arguments[1] as String
            editor
        }
        `when`(editor.apply()).then { true }

        boardRepository = BoardRepository(dao, prefs)
    }

    private fun clearData() {
        subscribedTopics = mutableSetOf()
        mockBoardsDataString = ""
    }

    @Test
    fun subscribe_subscribeBoards_subscribedBoardsAndTopics() {
        clearData()
        val board1 = Board(1, "music", "음악", "음악대학공지사항", "음대공지")
        val board2 = Board(2, "eltec", "공대", "공과대학공지사항", "공대공지")
        val board3 = Board(3, "eltec", "컴공", "컴퓨터공학과공지사항" ,"컴공공")

        boardRepository.subscribeBoards(listOf(board1, board2, board3))

        var subscribedBoards = boardRepository.getSubscribedBoardList()
        assertEquals(subscribedBoards.size, 3)
        assertEquals(subscribedBoards[0].boardId, board1.boardId)
        assertEquals(subscribedBoards[1].boardId, board2.boardId)
        assertEquals(subscribedBoards[2].boardId, board3.boardId)

        assertEquals(subscribedTopics.size, 2)
        assert(subscribedTopics.contains("music"))
        assert(subscribedTopics.contains("eltec"))


        val board4 = Board(4, "pharm", "약대", "약학대학공지사항", "약학공지")
        val board5 = Board(5, "Art", "조예대", "조예대학공지사항", "조예공지")

        boardRepository.subscribeBoards(listOf(board3, board4, board5))

        subscribedBoards = boardRepository.getSubscribedBoardList()
        assertEquals(subscribedBoards.size, 3)
        assertEquals(subscribedBoards[0].boardId, board3.boardId)
        assertEquals(subscribedBoards[1].boardId, board4.boardId)
        assertEquals(subscribedBoards[2].boardId, board5.boardId)

        assertEquals(subscribedTopics.size, 3)
        assert(subscribedTopics.contains("eltec"))
        assert(subscribedTopics.contains("pharm"))
        assert(subscribedTopics.contains("Art"))
        assertFalse(subscribedTopics.contains("music"))
    }
}