package com.example.notestasks.ui.notes

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notestasks.NotesTasksApplication
import com.example.notestasks.R
import com.example.notestasks.data.model.Note
import com.example.notestasks.databinding.FragmentNoteDetailBinding
import kotlinx.coroutines.launch

class NoteDetailFragment : Fragment() {

    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!
    private val args: NoteDetailFragmentArgs by navArgs()
    private lateinit var viewModel: NotesViewModel
    private var existingNote: Note? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = (requireActivity().application as NotesTasksApplication).noteRepository
        viewModel = ViewModelProvider(this, NotesViewModelFactory(repository))[NotesViewModel::class.java]

        if (args.noteId != -1L) {
            lifecycleScope.launch {
                existingNote = repository.getNoteById(args.noteId)
                existingNote?.let { note ->
                    binding.editTitle.setText(note.title)
                    binding.editContent.setText(note.content)
                }
            }
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_note_detail, menu)
                menu.findItem(R.id.action_delete).isVisible = args.noteId != -1L
            }
            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_save -> { saveNote(); true }
                    R.id.action_delete -> { deleteNote(); true }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveNote() {
        val title = binding.editTitle.text.toString().trim()
        val content = binding.editContent.text.toString().trim()

        if (title.isBlank() && content.isBlank()) {
            findNavController().navigateUp()
            return
        }

        val note = existingNote?.copy(
            title = title,
            content = content,
            updatedAt = System.currentTimeMillis()
        ) ?: Note(title = title, content = content)

        if (existingNote != null) viewModel.update(note)
        else viewModel.insert(note)

        findNavController().navigateUp()
    }

    private fun deleteNote() {
        existingNote?.let { viewModel.delete(it) }
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
