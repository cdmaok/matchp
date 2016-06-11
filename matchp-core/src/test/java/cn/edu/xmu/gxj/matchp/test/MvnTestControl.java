package cn.edu.xmu.gxj.matchp.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import cn.edu.xmu.gxj.matchp.l2r.RankListBuilderTest;
import cn.edu.xmu.gxj.matchp.model.DocFactoryTest;
import cn.edu.xmu.gxj.matchp.model.LoftTest;
import cn.edu.xmu.gxj.matchp.model.WeiboTest;
import cn.edu.xmu.gxj.matchp.score.EntryBuilderTest;

@RunWith(Suite.class)
@SuiteClasses({WeiboTest.class,
	LoftTest.class,
	EntryBuilderTest.class,
	DocFactoryTest.class,
	EntryBuilderTest.class,
	RankListBuilderTest.class})

public class MvnTestControl {

}
