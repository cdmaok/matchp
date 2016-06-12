package cn.edu.xmu.gxj.matchp.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import cn.edu.xmu.gxj.matchp.l2r.RankListBuilderTest;
import cn.edu.xmu.gxj.matchp.model.DocFactoryTest;
import cn.edu.xmu.gxj.matchp.model.LoftTest;
import cn.edu.xmu.gxj.matchp.model.WeiboTest;
import cn.edu.xmu.gxj.matchp.score.EntryUtilityTest;
import cn.edu.xmu.gxj.matchp.util.JsonUtility;
import cn.edu.xmu.gxj.matchp.util.JsonUtilityTest;

@RunWith(Suite.class)
@SuiteClasses({WeiboTest.class,
	LoftTest.class,
	EntryUtilityTest.class,
	JsonUtilityTest.class,
	DocFactoryTest.class,
	EntryUtilityTest.class,
	RankListBuilderTest.class})

public class MvnTestControl {

}
