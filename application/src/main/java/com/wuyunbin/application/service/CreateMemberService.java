package com.wuyunbin.application.service;

import com.wuyunbin.application.usecase.CreateMemberUseCase;
import com.wuyunbin.domain.Member;
import com.wuyunbin.port.MemberRepository;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateMemberService implements CreateMemberUseCase {
    @Resource
    private MemberRepository memberRepository;

    @Resource
    private ApplicationEventPublisher publisher;

    @Override
    public void execute(Member member) {
        // 获取一个Member领域模型,已通过模拟的数据实现
        // 调用领域模型提供的持久化方法
        memberRepository.save(member);

        member.create();

        List<Object> events = member.pullDomainEvents();

        if(!events.isEmpty()){
            //通过ApplicationEventPublisher发布事件
            events.forEach(e->{
                publisher.publishEvent(e);
            });
        }


    }
}
