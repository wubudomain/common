package top.wboost.common.boot;

import java.io.PrintStream;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import top.wboost.common.base.enums.CharsetEnum;
import top.wboost.common.util.RandomUtil;
import top.wboost.common.utils.web.utils.FileUtil;

public class CommonSpringApplication extends SpringApplication {

	public static ConfigurableApplicationContext run(Object source, String... args) {
		return run(new Object[] { source }, args);
	}

	public static ConfigurableApplicationContext run(Object[] sources, String[] args) {
		SpringApplication application = new SpringApplication(sources);
		application.setBanner((Environment environment, Class<?> sourceClass, PrintStream out) -> {
			try {
				Resource[] resources = new PathMatchingResourcePatternResolver()
						.getResources("classpath*:/sys/banners/*");
				int fileNum = resources.length;
				int num = RandomUtil.getRandomNum(fileNum);
				Resource resource = resources[num];
				String txt = FileUtil.importFile(resource.getInputStream(), CharsetEnum.UTF_8);
				out.print(txt);
				out.print("\n");
			} catch (Exception e) {

			}
		});
		ConfigurableApplicationContext context = application.run(args);
		return context;
	}

}
