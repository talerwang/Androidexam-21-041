package org.fattili.luckymusic.ui.view.main

import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.lm_main_fragment_home.lm_main_home_view_pager
import org.fattili.luckymusic.R
import org.fattili.luckymusic.ui.adapter.SectionsPagerAdapter
import org.fattili.luckymusic.ui.base.BaseFragment
import org.fattili.luckymusic.ui.view.main.play.LyricFragment
import org.fattili.luckymusic.ui.view.main.play.PlayFragment

class HomeFragment : BaseFragment() {

    override val layoutId: Int
        get() = R.layout.lm_main_fragment_home

    override fun initView() {
        lm_main_home_view_pager?.adapter = SectionsPagerAdapter(
            context, childFragmentManager,
            arrayOf(
                PlayFragment.newInstance(), // 播放页
                LyricFragment.newInstance() // 歌词页
            )
        )
    }

    override fun initData() {
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}